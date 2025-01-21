<!DOCTYPE html>
<html lang="{{ str_replace('_', '-', app()->getLocale()) }}">
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="csrf-token" content="{{ csrf_token() }}">

        <title>{{ config('app.name', 'Laravel') }}</title>

        <!-- Fonts -->
        <link rel="preconnect" href="https://fonts.bunny.net">
        <link href="https://fonts.bunny.net/css?family=figtree:400,500,600&display=swap" rel="stylesheet" />

        <!-- Bootstrap CSS -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css">

        <!-- Scripts -->
        @vite(['resources/css/app.css', 'resources/js/app.js'])

        <style>
             .top-bar {
                width: 100%;
                height: 60px;
                background-color: #f8f9fa; 
                box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1); 
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 0 20px;
                position: fixed;
                top: 0;
                left: 0;
                z-index: 1000;
            }
            .top-bar .app-name {
                font-size: 18px;
                font-weight: bold;
                color: #333;
            }
            .top-bar .user-name {
                font-size: 16px;
                color: #555;
            }

           
            .sidebar {
                width: 250px;
                height: calc(100vh - 60px); 
                position: fixed;
                top: 60px; 
                left: 0;
                background-color:rgb(55, 127, 156);
                color: white;
                padding-top: 20px;
                overflow-y: auto;
            }
            .sidebar a {
                color: white;
                text-decoration: none;
                padding: 10px 20px;
                display: block;
                border-radius: 5px;
                transition: all 0.3s ease;
            }
            .sidebar a.active,
            .sidebar a:hover {
                background-color: rgba(255, 255, 255, 0.1);
                box-shadow: 0px 0px 5px 2px rgba(255, 255, 255, 0.2);
            }

            
            .main-content {
                margin-left: 250px; 
                margin-top: 60px; 
                padding: 20px;
                flex-grow: 1; 
            }
            .container-fluid {
                width: 100%;
                padding: 0;
            }

            /* Ajustes responsivos */
            @media (max-width: 768px) {
                .sidebar {
                    width: 200px;
                }
                .main-content {
                    margin-left: 200px;
                }
            }

        </style>
    </head>
    <body>
        <div class="top-bar">
            <div class="app-name">MediMate</div>
            <div class="text-end">
    @auth
        <span>Bem-vindo, {{ Auth::user()->nombre }}</span>
    @else
        <span>Bem, Convidado</span>
    @endauth
</div>
    
        </div>
        <div class="d-flex">
            <!-- Menú lateral -->
            <div class="sidebar">
                <div class="text-center py-3">
                    @auth
                        <h5>{{ Auth::user()->name }}</h5>
                    @else
                        <h5>Invitado</h5>
                    @endauth
                </div>
                <ul class="nav flex-column px-3" style="gap: 10px;">
                    @auth
                        <li class="nav-item">
                            <a class="nav-link text-white {{ request()->routeIs('web.medicamentos.*') ? 'active' : '' }}" href="{{ route('web.medicamentos.index') }}">
                                Medicamentos
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white {{ request()->routeIs('web.recordatorios.*') ? 'active' : '' }}" href="{{ route('web.recordatorios.index') }}">
                                Recordatorios
                            </a>
                        </li>
                        <li class="nav-item">
               
                            <a class="nav-link text-white {{ request()->routeIs('web.historico.*') ? 'active' : '' }}" href="{{ route('web.historico.index') }}">
                                Histórico de Tomas
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white {{ request()->routeIs('alertas.index') ? 'active' : '' }}" href="{{ route('alertas.index') }}">
                                Alertas Ativas 
                            </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link text-white" href="{{ route('logout') }}" onclick="event.preventDefault(); document.getElementById('logout-form').submit();">
                                Logout
                            </a>
                            <form id="logout-form" action="{{ route('logout') }}" method="POST" style="display: none;">
                                @csrf
                            </form>
                        </li>
                    @else
                        <li class="nav-item">
                            <a class="nav-link text-white" href="{{ route('login') }}">Login</a>
                        </li>
                        @if (Route::has('register'))
                            <li class="nav-item">
                                <a class="nav-link text-white" href="{{ route('register') }}">Register</a>
                            </li>
                        @endif
                    @endauth
                </ul>
            </div>

            <!-- Conteudo principal -->
            <div class="main-content">
                <div class="container-fluid">
                    @yield('content')
                </div>
            </div>
        </div>

        <!-- Bootstrap JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

        <!-- Script para mostrar alertas -->
        <script>
            let ultimaVerificacion = Date.now();

            function verificarAlertas() {
                const ahora = Date.now();
                const intervaloEnMilisegundos = 60000; // 1 minuto

                if (ahora - ultimaVerificacion >= intervaloEnMilisegundos) {
                    ultimaVerificacion = ahora;

                    fetch('{{ route('alertas.pendientes') }}')
                        .then(response => response.json())
                        .then(data => {
                            if (data.alertas && data.alertas.length > 0) {
                                data.alertas.forEach(alerta => {
                                    alert(`Recordatorio: ${alerta.medicamento.nome} \nHorario: ${alerta.horario} \nFrecuencia: cada ${alerta.frequencia} minutos.`);
                                });
                            }
                        })
                        .catch(error => console.error('Erro ao verificar alertas:', error));
                }
            }

           
            setInterval(verificarAlertas, 60000); 
        </script>
    </body>
</html>

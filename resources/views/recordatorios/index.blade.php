@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <!-- Título centrado -->
    <div class="d-flex justify-content-center mb-4">
        <h1 class="fw-bold fs-2">Lista de Recordatorios</h1>
    </div>

   
    <div class="d-flex justify-content-between align-items-center mb-3">
        <!-- Formulario  -->
        <form method="GET" action="{{ route('web.recordatorios.index') }}" class="d-flex align-items-center mb-3">
            <label for="search" class="me-2 fw-bold">Procurar:</label>
            <div class="input-group">
                <input type="text" name="search" id="search" class="form-control" placeholder="Nome de medicamento" style="border-radius: 25px; border: 1px solid #ddd; padding: 10px 20px; box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);">
                <button type="submit" class="btn btn-primary ms-2">Procurar</button>
            </div>
        </form>
        <!-- Botton de adicionar recordatorio -->
        <a href="{{ route('web.recordatorios.create') }}" class="btn btn-primary">
            Adicionar Recordatorio
        </a>
    </div>

    <!-- Tabela de recordatorios -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>#</th>
                <th>Medicamento</th>
                <th>Horario</th>
                <th>Frequência</th>
                <th>Ativo</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($recordatorios as $recordatorio)
                <tr>
                    <td>{{ $loop->iteration }}</td>
                    <td>{{ $recordatorio->medicamento->nome }}</td>
                    <td>{{ $recordatorio->horario }}</td>
                    <td>{{ $recordatorio->frequencia }}</td>
                    <td>{{ $recordatorio->ativo ? 'Sí' : 'No' }}</td>
                    <td>
                        <a href="{{ route('web.recordatorios.edit', $recordatorio->id) }}" class="btn btn-warning btn-sm">Editar</a>
                        <form action="{{ route('web.recordatorios.destroy', $recordatorio->id) }}" method="POST" style="display:inline-block;">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Tem a certeza de eliminar este recordatorio?')">Eliminar</button>
                        </form>
                    </td>
                </tr>
            @empty
                <tr>
                    <td colspan="6" class="text-center">Não se encontraram recordatorios.</td>
                </tr>
            @endforelse
        </tbody>
    </table>

    
    <div class="d-flex justify-content-end">
        {{ $recordatorios->links() }}
    </div>
</div>
@endsection
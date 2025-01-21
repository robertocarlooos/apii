@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <!-- Título centrado -->
    <div class="d-flex justify-content-center mb-4">
        <h1 class="fw-bold fs-2">Histórico de Tomas</h1>
    </div>

    <!-- Campo de búsqueda -->
    <div class="d-flex justify-content-between align-items-center mb-3">
        <form method="GET" action="{{ route('web.historico.index') }}" class="d-flex align-items-center">
            <label for="search" class="me-2 fw-bold">Procurar:</label>
            <div class="input-group">
                <input type="text" name="search" id="search" class="form-control" placeholder="Nome de medicamento" style="border-radius: 25px; border: 1px solid #ddd; padding: 10px 20px; box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);">
                <button type="submit" class="btn btn-primary ms-2">Procurar</button>
            </div>
        </form>
    </div>

    <!-- Tabla de historial -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>ID</th>
                <th>Medicamento</th>
                <th>Data de Toma</th>
                <th>Hora de Toma</th>
                <th>Estado</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($historico as $registro)
                <tr>
                    <td>{{ $registro->id }}</td>
                    <td>{{ $registro->medicamento->nome }}</td>
                    <td>{{ $registro->data_toma }}</td>
                    <td>{{ $registro->hora_toma }}</td>
                    <td>{{ $registro->estado }}</td>
                </tr>
            @empty
                <tr>
                    <td colspan="5" class="text-center">Registos não encontrados.</td>
                </tr>
            @endforelse
        </tbody>
    </table>

    <!-- Paginación -->
    <div class="d-flex justify-content-end">
        {{ $historico->links() }}
    </div>
</div>
@endsection
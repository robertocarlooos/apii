@extends('layouts.app')

@section('content')
<div class="container mt-5">
    
    <div class="d-flex justify-content-center mb-4">
        <h1 class="fw-bold  fs-2">Lista de Medicamentos</h1>
    </div>

    
    <div class="d-flex justify-content-between align-items-center mb-3">
       
        <form method="GET" action="{{ route('web.medicamentos.index') }}" class="d-flex align-items-center mb-3">
    <label for="search" class="me-2 fw-bold">Procurar:</label>
    <div class="input-group">
        <input type="text" name="search" id="search" class="form-control" placeholder="Nome de medicamento" style="border-radius: 25px; border: 1px solid #ddd; padding: 10px 20px; box-shadow: 0px 2px 4px rgba(0, 0, 0, 0.1);">
        <button type="submit" class="btn btn-primary ms-2">Procurar</button>
    </div>
</form>
        <!-- Botton para adicionar medicamento -->
        <a href="{{ route('web.medicamentos.create') }}" class="btn btn-primary">
            Adicionar Medicamento
        </a>
    </div>

    <!-- Tabela de medicamentos -->
    <table class="table table-bordered">
        <thead>
            <tr>
                <th>#</th>
                <th>Nome</th>
                <th>Dose</th>
                <th>Tipo</th>
                <th>Quantidade atual</th>
                <th>Categoría</th>
                <th>Ações</th>
            </tr>
        </thead>
        <tbody>
            @forelse ($medicamentos as $medicamento)
                <tr>
                    <td>{{ $loop->iteration }}</td>
                    <td>{{ $medicamento->nome }}</td>
                    <td>{{ $medicamento->dose }}</td>
                    <td>{{ $medicamento->tipo }}</td>
                    <td>{{ $medicamento->quantidade_atual }}</td>
                    <td>{{ $medicamento->categoria->nome ?? 'Sin Categoría' }}</td>
                    <td>
                        <a href="{{ route('web.medicamentos.edit', $medicamento->id) }}" class="btn btn-warning btn-sm">Editar</a>
                        <form action="{{ route('web.medicamentos.destroy', $medicamento->id) }}" method="POST" style="display:inline-block;">
                            @csrf
                            @method('DELETE')
                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('¿Tem a certeza de eliminar este medicamento?')">Eliminar</button>
                        </form>
                        <form action="{{ route('web.medicamentos.marcarYReducir', $medicamento->id) }}" method="POST" style="display:inline;">
                            @csrf
                            <button type="submit" class="btn btn-success btn-sm">Marcar como Tomado</button>
                        </form>
                    </td>
                </tr>
            @empty
                <tr>
                    <td colspan="7" class="text-center">Não se encontraram medicamentos.</td>
                </tr>
            @endforelse
        </tbody>
    </table>

    <!-- Paginação -->
    <div class="d-flex justify-content-end">
        {{ $medicamentos->links() }}
    </div>
</div>
@endsection


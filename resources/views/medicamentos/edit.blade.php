@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <h1>Editar Medicamento</h1>
    <form action="{{ route('web.medicamentos.update', $medicamento->id) }}" method="POST">
        @csrf
        @method('PUT')

        <!-- Lista de medicamentos -->
        <div class="mb-3">
            <label for="medicamento_seleccionado" class="form-label">Selecionar Medicamento</label>
            <select class="form-select" id="medicamento_seleccionado" onchange="cargarDatosMedicamento(this)">
                <option value="">Selecione um medicamento</option>
                @foreach ($medicamentos as $med)
                    <option value="{{ $med->id }}" 
                        data-nome="{{ $med->nome }}"
                        data-dose="{{ $med->dose }}"
                        data-tipo="{{ $med->tipo }}"
                        data-quantidade="{{ $med->quantidade_atual }}"
                        {{ $medicamento->id == $med->id ? 'selected' : '' }}>
                        {{ $med->nome }}
                    </option>
                @endforeach
            </select>
        </div>

        <!-- Formulario de edição -->
        <div class="mb-3">
            <label for="nome" class="form-label">Nome</label>
            <input type="text" class="form-control" id="nome" name="nome" value="{{ $medicamento->nome }}">
        </div>
        <div class="mb-3">
            <label for="dose" class="form-label">Dose</label>
            <input type="text" class="form-control" id="dose" name="dose" value="{{ $medicamento->dose }}">
        </div>
        <div class="mb-3">
            <label for="tipo" class="form-label">Tipo</label>
            <input type="text" class="form-control" id="tipo" name="tipo" value="{{ $medicamento->tipo }}">
        </div>
        <div class="mb-3">
            <label for="quantidade_atual" class="form-label">Quntidade Atual</label>
            <input type="number" class="form-control" id="quantidade_atual" name="quantidade_atual" value="{{ $medicamento->quantidade_atual }}">
        </div>

        <!-- Selecionar  Categorías -->
        <div class="mb-3">
            <label for="categoria_id" class="form-label">Categoría</label>
            <select class="form-select" id="categoria_id" name="categoria_id" onchange="mostrarDescripcionCategoria(this)">
                <option value="">Selecione uma categoría</option>
                @foreach ($categorias as $categoria)
                    <option value="{{ $categoria->id }}" data-descricao="{{ $categoria->descricao }}"
                        {{ $medicamento->categoria_id == $categoria->id ? 'selected' : '' }}>
                        {{ $categoria->nome }}
                    </option>
                @endforeach
            </select>
        </div>

        <!-- Descrição da Categoría -->
        <div class="mb-3">
            <label class="form-label">Descrição da Categoría</label>
            <p id="categoria_descricao" class="form-control" readonly>
                {{ $medicamento->categoria->descricao ?? 'Selecione uma categoría para ver a descrição' }}
            </p>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
    </form>
</div>

<script>
    function cargarDatosMedicamento(select) {
        const option = select.options[select.selectedIndex];
        document.getElementById('nome').value = option.getAttribute('data-nome');
        document.getElementById('dose').value = option.getAttribute('data-dose');
        document.getElementById('tipo').value = option.getAttribute('data-tipo');
        document.getElementById('quantidade_atual').value = option.getAttribute('data-quantidade');
       
    }

    function mostrarDescripcionCategoria(select) {
        const option = select.options[select.selectedIndex];
        const descripcion = option.getAttribute('data-descricao') || 'Seleccione uma categoría para ver a descrição';
        document.getElementById('categoria_descricao').innerText = descripcion;
    }

</script>
@endsection

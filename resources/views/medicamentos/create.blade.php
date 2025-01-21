@extends('layouts.app')

@section('content')

<div class="container mt-5">
    <h1>Adicionar Novo Medicamento</h1>
    
    <a href="{{ route('web.medicamentos.index') }}" class="btn btn-secondary mb-3">&larr; Voltar </a>
    
    <div class="card">
        <div class="card-header">
            Novo Medicamento
        </div>
        <div class="card-body">
            <form action="{{ route('web.medicamentos.store') }}" method="POST">
                @csrf
                
                <div class="mb-3">
                    <label for="nome" class="form-label">Nome do Medicamento</label>
                    <input type="text" class="form-control @error('nome') is-invalid @enderror" id="nome" name="nome" value="{{ old('nome') }}" placeholder="Adicione o nome">
                    @error('nome')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                
                <div class="mb-3">
                    <label for="dose" class="form-label">Dose</label>
                    <input type="text" class="form-control @error('dose') is-invalid @enderror" id="dose" name="dose" value="{{ old('dose') }}" placeholder="Insira a dose (ex: 500mg)">
                    @error('dose')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                
                <div class="mb-3">
                    <label for="tipo" class="form-label">Tipo</label>
                    <input type="text" class="form-control @error('tipo') is-invalid @enderror" id="tipo" name="tipo" value="{{ old('tipo') }}" placeholder="Exemplo: Comprimido, Xarope etc...">
                    @error('tipo')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                
                <div class="mb-3">
                    <label for="quantidade_atual" class="form-label">Quantidade Atual</label>
                    <input type="number" class="form-control @error('quantidade_atual') is-invalid @enderror" id="quantidade_atual" name="quantidade_atual" value="{{ old('quantidade_atual') }}" placeholder="Insira a quantidade atual">
                    @error('quantidade_atual')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>

                <div class="mb-3">
                    <label for="categoria_id" class="form-label">Categoría</label>
                    <select class="form-control @error('categoria_id') is-invalid @enderror" id="categoria_id" name="categoria_id" onchange="mostrarDescripcion()">
                        <option value="">Selecione uma categoría</option>
                        @foreach ($categorias as $categoria)
                            <option value="{{ $categoria->id }}" data-descripcion="{{ $categoria->descricao }}" {{ old('categoria_id', $medicamento->categoria_id ?? '') == $categoria->id ? 'selected' : '' }}>
                                {{ $categoria->nome }}
                            </option>
                        @endforeach
                    </select>
                    @error('categoria_id')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>

                <div id="descripcion-categoria" class="mt-2 text-muted"></div>
        
                <button type="submit" class="btn btn-primary">Guardar Medicamento</button>
            </form>
        </div>
    </div>
</div>

<script>
    function mostrarDescripcion() {
        const select = document.getElementById('categoria_id');
        const descripcion = select.options[select.selectedIndex].getAttribute('data-descripcion');
        document.getElementById('descripcion-categoria').innerText = descripcion ? descripcion : '';
    }
</script>

@endsection
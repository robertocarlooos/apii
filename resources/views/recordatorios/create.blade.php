@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <h1>Adicionar Novo Recordatório</h1>
    <a href="{{ route('web.recordatorios.index') }}" class="btn btn-secondary mb-3">&larr; Voltar à Lista</a>
    <div class="card">
        <div class="card-header">
            Formulario de Novo Recordatório
        </div>
        <div class="card-body">
            <form action="{{ route('web.recordatorios.store') }}" method="POST">
                @csrf
                <div class="mb-3">
                    <label for="medicamento_id" class="form-label">Medicamento</label>
                    <select class="form-control @error('medicamento_id') is-invalid @enderror" id="medicamento_id" name="medicamento_id">
                        <option value="">Selecione um medicamento</option>
                        @foreach ($medicamentos as $medicamento)
                            {{-- Verifica que el medicamento pertenece al usuario autenticado --}}
                            <option value="{{ $medicamento->id }}" {{ old('medicamento_id') == $medicamento->id ? 'selected' : '' }}>
                                {{ $medicamento->nome }}
                            </option>
                        @endforeach
                    </select>
                    @error('medicamento_id')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                <div class="mb-3">
                    <label for="horario" class="form-label">Horario</label>
                    <input type="time" class="form-control @error('horario') is-invalid @enderror" id="horario" name="horario" value="{{ old('horario') }}">
                    @error('horario')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                <div class="mb-3">
                    <label for="frequencia" class="form-label">Frequência</label>
                    <input type="text" class="form-control @error('frequencia') is-invalid @enderror" id="frequencia" name="frequencia" value="{{ old('frequencia') }}">
                    @error('frequencia')
                        <span class="text-danger">{{ $message }}</span>
                    @enderror
                </div>
                <div class="form-check mb-3">
                    <input class="form-check-input" type="checkbox" id="ativo" name="ativo" value="1" {{ old('ativo') ? 'checked' : '' }}>
                    <label class="form-check-label" for="ativo">
                        Ativo
                    </label>
                </div>
                <button type="submit" class="btn btn-primary">Guardar</button>
            </form>
        </div>
    </div>
</div>
@endsection
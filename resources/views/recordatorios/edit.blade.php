@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <h1>Editar Recordatorio</h1>
    <a href="{{ route('web.recordatorios.index') }}" class="btn btn-secondary mb-3">&larr; Volver</a>
    <div class="card">
        <div class="card-header">Formulario de Edición</div>
        <div class="card-body">
            <form action="{{ route('web.recordatorios.update', $recordatorio->id) }}" method="POST">
                @csrf
                @method('PATCH')

                <div class="mb-3">
                    <label for="medicamento_id" class="form-label">Medicamento</label>
                    <select name="medicamento_id" id="medicamento_id" class="form-control">
                        @foreach ($medicamentos as $medicamento)
                            <option value="{{ $medicamento->id }}" {{ $recordatorio->medicamento_id == $medicamento->id ? 'selected' : '' }}>
                                {{ $medicamento->nome }}
                            </option>
                        @endforeach
                    </select>
                </div>

                <div class="mb-3">
                    <label for="horario" class="form-label">Horario</label>
                    <input type="time" name="horario" id="horario" class="form-control" value="{{ $recordatorio->horario }}">
                </div>

                <div class="mb-3">
                    <label for="frequencia" class="form-label">Frecuencia (minutos)</label>
                    <input type="number" name="frequencia" id="frequencia" class="form-control" value="{{ $recordatorio->frequencia }}">
                </div>

                <div class="mb-3">
                    <label for="ativo" class="form-label">Ativo</label>
                    <select name="ativo" id="ativo" class="form-control">
                        <option value="1" {{ $recordatorio->ativo ? 'selected' : '' }}>Sí</option>
                        <option value="0" {{ !$recordatorio->ativo ? 'selected' : '' }}>No</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary">Guardar Cambios</button>
            </form>
        </div>
    </div>
</div>
@endsection
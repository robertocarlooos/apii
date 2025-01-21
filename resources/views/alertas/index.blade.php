@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <h1>Alertas </h1>
    
    @if ($alertas->isEmpty())
        <p class="text-muted">Não ha alertas neste momento.</p>
    @else
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Medicamento</th>
                    <th>Horario</th>
                    <th>Frequência (minutos)</th>
                </tr>
            </thead>
            <tbody>
                @foreach ($alertas as $alerta)
                    <tr>
                        <td>{{ $loop->iteration }}</td>
                        <td>{{ $alerta->medicamento->nome }}</td> 
                        <td>{{ $alerta->horario }}</td>
                        <td>{{ $alerta->frequencia }}</td>
                    </tr>
                @endforeach
            </tbody>
        </table>
    @endif
</div>
@endsection
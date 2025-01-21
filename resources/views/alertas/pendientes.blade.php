@extends('layouts.app')

@section('content')
<div class="container mt-5">
    <h1>Alertas </h1>
    <ul class="list-group">
        @forelse ($alertas as $alerta)
            <li class="list-group-item">{{ $alerta }}</li>
        @empty
            <li class="list-group-item">Não ha alertas.</li>
        @endforelse
    </ul>
</div>
@endsection
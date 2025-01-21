<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreHistorialTomaRequest;
use App\Http\Resources\HistorialTomaResource;
use App\Models\HistorialToma;

class HistorialTomaController extends Controller
{
    public function index()
    {
                 // Construimos la consulta base
    $query = \App\Models\HistorialToma::whereHas('medicamento', function ($query) use ($request) {
        $query->where('usuario_id', auth()->id());

        // Agregamos la funcionalidad de búsqueda por nombre del medicamento
        if ($request->filled('search')) {
            $query->where('nome', 'LIKE', '%' . $request->search . '%');
        }
    })->with('medicamento');

    
    $historico = $query->paginate(6);

    return view('historico.index', compact('historico'));
    }



    public function store(StoreHistorialTomaRequest $request)
    {
        $historialToma = HistorialToma::create($request->validated());
        return new HistorialTomaResource($historialToma);
    }



    public function destroy(HistorialToma $historialToma)
    {
        $historialToma->delete();
        return response()->json(['message' => 'Historial eliminado correctamente'], 204);
    }




    public function getHistorial()
{
    $historial = HistorialToma::with('medicamento') // Incluye el medicamento relacionado
        ->whereHas('medicamento', function ($query) {
            $query->where('usuario_id', auth()->id());
        })
        ->get();

    return response()->json(['data' => $historial]);
}
  
}
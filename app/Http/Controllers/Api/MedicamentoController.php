<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Resources\MedicamentoResource;
use App\Models\Medicamento;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class MedicamentoController extends Controller
{
    // Listar medicamentos con búsqueda y paginación
    public function index(Request $request)
    {
        // Asegurarse de cargar la relación 'categoria'
    $query = Medicamento::with('categoria')->where('usuario_id', auth()->id());

    // Filtro de búsqueda
    if ($request->filled('search')) {
        $query->where('nome', 'LIKE', '%' . $request->search . '%');
    }

    // Paginación o retorno de todos los datos
    $medicamentos = $query->get(); // Puedes usar paginate(6) si necesitas paginación

    return response()->json([
        'data' => $medicamentos,
    ]);
    }

    // Crear un nuevo medicamento
    public function store(Request $request)
    {
       
        $validatedData = $request->validate([
            'nome' => 'required|string|max:255',
            'dose' => 'required|string|max:255',
            'tipo' => 'required|string|max:255',
            'quantidade_atual' => 'required|integer|min:0',
            'categoria_id' => 'nullable|exists:categorias,id',
        ]);
    
        $validatedData['usuario_id'] = auth()->id(); // Si usas autenticación
    
        $medicamento = Medicamento::create($validatedData);
    
        return response()->json([
            'message' => 'Medicamento añadido correctamente',
            'medicamento' => $medicamento,
        ], 201);
    }

    // Mostrar un medicamento
    public function show(Medicamento $medicamento)
    {
        // Verificar que el medicamento pertenece al usuario autenticado
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'No tienes permiso para ver este medicamento.');
        }

        return new MedicamentoResource($medicamento);
    }

    // Actualizar un medicamento
    public function update(Request $request, Medicamento $medicamento)
    {
        $validatedData = $request->validate([
            'nome' => 'required|string|max:255',
            'dose' => 'required|string|max:255',
            'tipo' => 'required|string|max:255',
            'quantidade_atual' => 'required|integer|min:0',
            
            'categoria_id' => 'nullable|exists:categorias,id',
        ]);

        // Verificar que el medicamento pertenece al usuario autenticado
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'No tienes permiso para actualizar este medicamento.');
        }

        $medicamento->update($validatedData);

        return new MedicamentoResource($medicamento);
    }

    // Eliminar un medicamento
    public function destroy(Medicamento $medicamento)
    {
        // Verificar que el medicamento pertenece al usuario autenticado
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'No tienes permiso para eliminar este medicamento.');
        }

        $medicamento->delete();

        return response()->json(['message' => 'Medicamento eliminado con éxito'], 204);
    }

    // Reducir la cantidad de un medicamento y marcarlo como tomado
    public function marcarYReducir(Request $request, $id)
    {
        // Buscar el medicamento por ID y verificar que pertenece al usuario autenticado
        $medicamento = Medicamento::where('id', $id)
            ->where('usuario_id', auth()->id())
            ->firstOrFail();

        // Reducir la cantidad actual en 1 si es mayor que 0
        if ($medicamento->quantidade_atual > 0) {
            $medicamento->quantidade_atual -= 1;
            $medicamento->save();
        }

        // Registrar en el historial de tomas
        \App\Models\HistorialToma::create([
            'medicamento_id' => $medicamento->id,
            'data_toma' => now()->toDateString(),
            'hora_toma' => now()->toTimeString(),
            'estado' => 'Tomado',
        ]);

        return response()->json([
            'message' => 'Medicamento marcado como tomado y cantidad actualizada.',
            'medicamento' => new MedicamentoResource($medicamento),
        ]);
    }

}

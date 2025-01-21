<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreRecordatorioRequest;
use App\Http\Resources\RecordatorioResource;
use App\Models\Recordatorio;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class RecordatorioApiController extends Controller
{
    public function index()
    {
        $recordatorios = Recordatorio::with('medicamento:id,nome')->get();

        return response()->json(['data' => $recordatorios]);
    }

    public function store(Request $request)
    {
        try {
            // Validar los datos de la solicitud
            $validatedData = $request->validate([
                'medicamento_id' => 'required|exists:medicamentos,id', // Verifica que el medicamento exista
                'horario' => 'required|date_format:H:i', // Valida el formato de hora
                'frequencia' => 'nullable|integer|min:1', // Frecuencia opcional, debe ser un número
                'ativo' => 'required|boolean', // Debe ser true o false
            ]);

            // Asocia el usuario autenticado, si aplica
            $validatedData['usuario_id'] = auth()->id();

            // Crea el recordatorio
            $recordatorio = Recordatorio::create($validatedData);

            // Retorna una respuesta en JSON
            return response()->json([
                'message' => 'Recordatorio añadido correctamente',
                'recordatorio' => $recordatorio,
            ], 201); // Código HTTP 201 para creación
        } catch (\Exception $e) {
            return response()->json([
                'error' => 'Error al guardar el recordatorio',
                'details' => $e->getMessage(),
            ], 500); // Código HTTP 500 para errores del servidor
        }
    }
    public function show(Recordatorio $recordatorio)
    {
        return new RecordatorioResource($recordatorio);
    }

    public function update(StoreRecordatorioRequest $request, Recordatorio $recordatorio)
    {
        $validatedData = $request->validate([
            'horario' => 'required|date_format:H:i',
            'frequencia' => 'nullable|integer|min:1',
            'ativo' => 'required|boolean',
        ]);
    
        $recordatorio->update($validatedData);
    
        return response()->json([
            'message' => 'Recordatorio actualizado correctamente',
            'recordatorio' => $recordatorio,
        ]);
    }

    public function destroy(Recordatorio $recordatorio)
    {
        $recordatorio->delete();
        return response()->json(['message' => 'Recordatorio eliminado con éxito'], 204);
    }
}
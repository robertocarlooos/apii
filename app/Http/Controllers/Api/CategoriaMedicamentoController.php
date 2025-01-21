<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreCategoriaMedicamentoRequest;
use Illuminate\Support\Facades\DB;

class CategoriaMedicamentoController extends Controller
{
    public function store(StoreCategoriaMedicamentoRequest $request)
    {
        DB::table('categoria_medicamento')->insert([
            'categoria_id' => $request->categoria_id,
            'medicamento_id' => $request->medicamento_id,
            'created_at' => now(),
            'updated_at' => now(),
        ]);

        return response()->json(['message' => 'Relação criada com éxito'], 201);
    }

    public function destroy(Request $request)
    {
        DB::table('categoria_medicamento')
            ->where('categoria_id', $request->categoria_id)
            ->where('medicamento_id', $request->medicamento_id)
            ->delete();

        return response()->json(['message' => 'Relação eliminada com éxito'], 204);
    }
}
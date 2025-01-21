<?php

namespace App\Http\Controllers\Api;

use App\Http\Controllers\Controller;
use App\Http\Requests\StoreCategoriaRequest;
use App\Http\Resources\CategoriaResource;
use App\Models\Categoria;

class CategoriaController extends Controller
{
    public function index()
    {
        $categorias = Categoria::all();
        return response()->json($categorias);
    }

    public function store(StoreCategoriaRequest $request)
    {
        $categoria = Categoria::create($request->validated());
        return new CategoriaResource($categoria);
    }

    public function show(Categoria $categoria)
    {
        return new CategoriaResource($categoria);
    }

    public function update(StoreCategoriaRequest $request, Categoria $categoria)
    {
        $categoria->update($request->validated());
        return new CategoriaResource($categoria);
    }

    public function destroy(Categoria $categoria)
    {
        $categoria->delete();
        return response()->json(['message' => 'Categoría eliminada com éxito'], 204);
    }
}
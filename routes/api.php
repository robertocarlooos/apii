<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\UsuarioController;
use App\Http\Controllers\Api\MedicamentoController;
use App\Http\Controllers\Api\RecordatorioApiController;
use App\Http\Controllers\Api\HistorialTomaController;
use App\Http\Controllers\Api\HistorialMedicamentoController;
use App\Http\Controllers\Api\CategoriaController;
use App\Http\Controllers\Api\CategoriaMedicamentoController;
use App\Http\Controllers\Api\ConfiguracaoRecordatorioController;



Route::post('login', [UsuarioController::class, 'login']);
Route::post('register', [UsuarioController::class, 'store']);
Route::post('usuarios', [UsuarioController::class, 'store']);
Route::get('/categorias', [CategoriaController::class, 'index']);
Route::post('/recordatorios', [RecordatorioApiController::class, 'store']);
Route::put('recordatorios/{recordatorio}', [RecordatorioApiController::class, 'update']);


Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
    return $request->user();
});


Route::apiResource('usuarios', UsuarioController::class)->only(['store', 'index', 'show']);
Route::apiResource('categorias', CategoriaController::class);
Route::post('categoria-medicamento', [CategoriaMedicamentoController::class, 'store']);
Route::delete('categoria-medicamento', [CategoriaMedicamentoController::class, 'destroy']);
Route::delete('historial_tomas/{historial_toma}', [HistorialTomaController::class, 'destroy']);


Route::middleware('auth:sanctum')->group(function () {
    
    Route::apiResource('medicamentos', MedicamentoController::class);
    Route::post('/medicamentos/{id}/marcar-y-reducir', [MedicamentoController::class, 'marcarYReducir']);

   
    Route::apiResource('recordatorios', RecordatorioApiController::class);

 
    Route::apiResource('historial_tomas', HistorialTomaController::class);
    Route::get('historico', [HistorialTomaController::class, 'getHistorial']);
    Route::apiResource('historial_medicamentos', HistorialMedicamentoController::class)->only(['index', 'store']);

    
    Route::put('configuracao-recordatorios/{configuracao}', [ConfiguracaoRecordatorioController::class, 'update']);
});
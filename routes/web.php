<?php

use App\Http\Controllers\ProfileController;
use App\Http\Controllers\Web\MedicamentoController; // Importa el controlador de medicamentos
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Auth\RegisteredUserController;
use App\Http\Controllers\Web\RecordatoriowebController;
use App\Http\Controllers\AlertaController;
use App\Http\Controllers\Web\HistoricoController;


Route::get('/', function () {
    return view('welcome');
});

Route::get('/dashboard', function () {
    return redirect()->route('web.medicamentos.index'); 
})->middleware(['auth', 'verified'])->name('dashboard');

Route::middleware('auth')->group(function () {
    Route::get('/profile', [ProfileController::class, 'edit'])->name('profile.edit');
    Route::patch('/profile', [ProfileController::class, 'update'])->name('profile.update');
    Route::delete('/profile', [ProfileController::class, 'destroy'])->name('profile.destroy');

   
    Route::resource('medicamentos', MedicamentoController::class)
        ->names('web.medicamentos'); 
});

require __DIR__.'/auth.php';

Route::get('/register', [RegisteredUserController::class, 'create'])->name('register');
Route::post('/register', [RegisteredUserController::class, 'store']);




 
 Route::resource('recordatorios', RecordatoriowebController::class)->names('web.recordatorios');
 Route::patch('recordatorios/{id}/toggle', [RecordatoriowebController::class, 'toggle'])->name('web.recordatorios.toggle');
 Route::delete('recordatorios/{recordatorio}', [RecordatoriowebController::class, 'destroy'])->name('web.recordatorios.destroy');

 Route::post('/medicamentos/{id}/marcar-tomado', [MedicamentoController::class, 'marcarTomado'])->name('web.medicamentos.marcarTomado');


Route::get('/alertas', [AlertaController::class, 'index'])->name('alertas.index');


Route::get('/alertas/verificar', [AlertaController::class, 'verificarAlertas'])->name('alertas.verificar');
Route::get('/alertas/pendientes', [AlertaController::class, 'pendientes'])->name('alertas.pendientes');



Route::post('/medicamentos/{id}/reducir-cantidad', [MedicamentoController::class, 'reducirCantidad'])->name('web.medicamentos.reducirCantidad');
  
Route::post('/medicamentos/{id}/marcar-y-reducir', [MedicamentoController::class, 'marcarYReducir'])->name('web.medicamentos.marcarYReducir');
Route::get('/historico', [HistoricoController::class, 'index'])->name('web.historico.index');


Route::get('/recordatorios/{recordatorio}/editar', [RecordatoriowebController::class, 'edit'])->name('web.recordatorios.edit');


Route::patch('/recordatorios/{recordatorio}', [RecordatoriowebController::class, 'update'])->name('web.recordatorios.update');
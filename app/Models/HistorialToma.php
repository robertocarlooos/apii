<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class HistorialToma extends Model
{
    use HasFactory;

    
    protected $fillable = ['medicamento_id', 'data_toma', 'hora_toma', 'estado'];

  
    public function medicamento()
    {
        return $this->belongsTo(Medicamento::class);
    }
    public function getHistorial()
{
    $historial = \App\Models\HistorialToma::with('medicamento')->where('usuario_id', auth()->id())->get();

    return response()->json($historial);
}
}

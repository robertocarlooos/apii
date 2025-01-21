<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Medicamento extends Model
{
    use HasFactory;

    
    protected $fillable = [
        'usuario_id', 'nome', 'dose', 'tipo',
        'quantidade_atual', 'alerta_running_low','categoria_id',
    ];

   
    public function usuario()
    {
         return $this->belongsTo(Usuario::class, 'usuario_id');
    }

    
    public function recordatorios()
    {
        return $this->hasMany(Recordatorio::class);
    }

   
    public function historialTomas()
    {
        return $this->hasMany(HistorialToma::class);
    }

    
    public function categoria()
    {
        return $this->belongsTo(Categoria::class, 'categoria_id');
    }
}
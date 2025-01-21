<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Recordatorio;
use Carbon\Carbon;

class AlertaController extends Controller
{
    public function verificarAlertas()
    {
        $now = Carbon::now();
        $alertas = Recordatorio::where('ativo', 1)
            ->get()
            ->filter(function ($recordatorio) use ($now) {
                $horaRecordatorio = Carbon::createFromTimeString($recordatorio->horario);
                $frecuencia = (int) filter_var($recordatorio->frequencia, FILTER_SANITIZE_NUMBER_INT);
                $diferencia = $horaRecordatorio->diffInMinutes($now, false);

                return $frecuencia > 0 && $diferencia >= 0 && $diferencia % $frecuencia === 0;
            });

        return response()->json(['alertas' => $alertas->values()]);
    }

    
    public function index()
    {
        $now = Carbon::now();
        $alertas = Recordatorio::where('ativo', 1)
            ->get()
            ->filter(function ($recordatorio) use ($now) {
                $horaRecordatorio = Carbon::createFromTimeString($recordatorio->horario);
                $frecuencia = (int) filter_var($recordatorio->frequencia, FILTER_SANITIZE_NUMBER_INT);
                $diferencia = $horaRecordatorio->diffInMinutes($now, false);

                return $frecuencia > 0 && $diferencia >= 0 && $diferencia % $frecuencia === 0;
            });

        return view('alertas.index', compact('alertas'));
    }

  
    
    public function pendientes()
    {
        $now = Carbon::now();

        $alertas = Recordatorio::where('ativo', 1)
            ->with('medicamento') 
            ->get()
            ->filter(function ($recordatorio) use ($now) {
                $horaRecordatorio = Carbon::parse($recordatorio->horario);
                $frecuenciaEnMinutos = (int) filter_var($recordatorio->frequencia, FILTER_SANITIZE_NUMBER_INT);
    
                // Calcula a diferença em minutos
                $diferenciaEnMinutos = $horaRecordatorio->diffInMinutes($now, false);
    
                // Verifica se está dentro do intervalo e se é o momento exato
                return $frecuenciaEnMinutos > 0 &&
                    $diferenciaEnMinutos >= 0 &&
                    $diferenciaEnMinutos % $frecuenciaEnMinutos === 0;
            });
    
        return response()->json(['alertas' => $alertas->values()]);
    }
}
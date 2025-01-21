<?php


namespace App\Console\Commands;

use Illuminate\Console\Command;
use App\Models\Recordatorio;
use App\Notifications\MedicamentoNotificacion;
use Carbon\Carbon;

class EnviarRecordatorio extends Command
{
    protected $signature = 'recordatorios:enviar';

    protected $description = 'Enviar alertas de recordatorios para medicamentos';

    public function __construct()
    {
        parent::__construct();
    }

    public function handle()
    {
        $now = Carbon::now();

        
        $recordatorios = Recordatorio::where('ativo', 1)
            ->with(['medicamento.usuario']) 
            ->get();

        foreach ($recordatorios as $recordatorio) {
            // Verifica si el recordatorio tiene un medicamento y un usuario asociado
            if (!$recordatorio->medicamento || !$recordatorio->medicamento->usuario) {
                $this->error("o recordatorio con ID {$recordatorio->id} não tem um medicamento ou um usuário associado.");
                continue;
            }

            $horaRecordatorio = Carbon::createFromTimeString($recordatorio->horario);
            $diferencia = $horaRecordatorio->diffInHours($now, false);

            
            $frecuencia = (int) filter_var($recordatorio->frequencia, FILTER_SANITIZE_NUMBER_INT);

            
            if ($frecuencia > 0 && $diferencia % $frecuencia === 0) {
                $recordatorio->medicamento->usuario->notify(new MedicamentoNotificacion($recordatorio));
            }
        }

        $this->info('Recordatorios procesados com éxito.');
    }
}
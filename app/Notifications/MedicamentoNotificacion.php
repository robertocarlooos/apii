<?php

namespace App\Notifications;

use Illuminate\Bus\Queueable;
use Illuminate\Notifications\Notification;

class MedicamentoNotificacion extends Notification
{
    use Queueable;

    protected $recordatorio;

    public function __construct($recordatorio)
    {
        $this->recordatorio = $recordatorio;
    }

    public function via($notifiable)
    {
        return ['database'];
    }

    public function toDatabase($notifiable)
    {
        return [
            'titulo' => 'Recordatorio de Medicamento',
            'mensaje' => "Es hora de tomar el medicamento {$this->recordatorio->medicamento->nome}.",
            'hora' => now(),
        ];
    }
}
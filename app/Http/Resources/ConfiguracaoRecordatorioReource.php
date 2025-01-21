<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class ConfiguracaoRecordatorioReource extends JsonResource
{
    /**
     * Transform the resource into an array.
     *
     * @return array<string, mixed>
     */
    public function toArray(Request $request): array
    {
        return [
            'id' => $this->id,
            'usuario_id' => $this->usuario_id,
            'notificacoes' => $this->notificacoes,
            'repetir_ate_tomar' => $this->repetir_ate_tomar,
            'numero_repeticoes' => $this->numero_repeticoes,
            'created_at' => $this->created_at,
            'updated_at' => $this->updated_at,
        ];
    }
}

<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class MedicamentoResource extends JsonResource
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
            'nome' => $this->nome,
            'dose' => $this->dose,
            'tipo' => $this->tipo,
            'quantidade_atual' => $this->quantidade_atual,
            'alerta_running_low' => $this->alerta_running_low,
            'created_at' => $this->created_at,
            'updated_at' => $this->updated_at,
        ];
    }
}

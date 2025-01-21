<?php

namespace App\Http\Resources;

use Illuminate\Http\Request;
use Illuminate\Http\Resources\Json\JsonResource;

class HistorialMedicamentoResource extends JsonResource
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
            'medicamento_id' => $this->medicamento_id,
            'data_adicionado' => $this->adicionado,
            'created_at' => $this->created_at,
            'updated_at' => $this->updated_at,
        ];
    }
}

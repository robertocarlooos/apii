<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreHistorialTomaRequest extends FormRequest
{
    /**
     * Determine if the user is authorized to make this request.
     */
    public function authorize(): bool
    {
        return true;
    }

    /**
     * Get the validation rules that apply to the request.
     *
     * @return array<string, \Illuminate\Contracts\Validation\ValidationRule|array<mixed>|string>
     */
    public function rules(): array
    {
        return [
            'medicamento_id' => ['required', 'exists:medicamentos,id'],
            'data_toma' => ['required', 'date'],
            'hora_toma' => ['required', 'date_format:H:i'],
            'estado' => ['required', 'string', 'max:255'],
        ];
        
    }
}

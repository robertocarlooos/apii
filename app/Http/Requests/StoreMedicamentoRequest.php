<?php

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;

class StoreMedicamentoRequest extends FormRequest
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
            'usuario_id' => ['required', 'exists:usuarios,id'],
            'nome' => ['required', 'string', 'max:255'],
            'dose' => ['required', 'string', 'max:255'],
            'tipo' => ['required', 'string', 'max:255'],
            'quantidade_atual' => ['required', 'integer', 'min:0'],
            'alerta_running_low' => ['sometimes', 'boolean'],
        ];
    }
}

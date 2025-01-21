<?php

namespace App\Http\Controllers\Web;
use App\Http\Controllers\Controller; 
use App\Models\Medicamento;
use App\Models\Recordatorio;
use Illuminate\Http\Request;

class RecordatoriowebController extends Controller
{
    public function index(Request $request)
    {
       
    $query = Recordatorio::whereHas('medicamento', function ($query) use ($request) {
        $query->where('usuario_id', auth()->id());

       
        if ($request->filled('search')) {
            $query->where('nome', 'LIKE', '%' . $request->search . '%');
        }
    })->with('medicamento');

    
    $recordatorios = $query->paginate(6);

    return view('recordatorios.index', compact('recordatorios'));
    }

    public function create()
    {
        $medicamentos = Medicamento::where('usuario_id', auth()->id())->get();
        return view('recordatorios.create', compact('medicamentos'));
    }

    public function store(Request $request)
    {
       
    $validatedData = $request->validate([
        'medicamento_id' => 'required|exists:medicamentos,id',
        'horario' => 'required',
        'frequencia' => 'required|string|max:255',
        'ativo' => 'nullable|boolean',
    ]);

    
    Recordatorio::create($validatedData);

   
    return redirect()->route('web.recordatorios.index')->with('success', 'Recordatorio criado com éxito.');
    }

    public function edit(Recordatorio $recordatorio)
    {
        $medicamentos = \App\Models\Medicamento::all();

        return view('recordatorios.edit', compact('recordatorio', 'medicamentos'));
    }

    public function update(Request $request, Recordatorio $recordatorio)
    {
        
        $validatedData = $request->validate([
            'medicamento_id' => 'required|exists:medicamentos,id',
            'horario' => 'required|date_format:H:i',
            'frequencia' => 'required|integer|min:1',
            'ativo' => 'required|boolean',
        ]);
    
        $recordatorio->update($validatedData);
    
       
        return redirect()->route('web.recordatorios.index')->with('success', 'Recordatorio atualizado com éxito.');
    }

    public function destroy($id)
    {
        $recordatorio = Recordatorio::findOrFail($id);
        $recordatorio->delete();
    
        return redirect()->route('web.recordatorios.index')->with('success', 'Recordatorio eliminado co, éxito.');
    }
    public function toggle($id)
{
    $recordatorio = Recordatorio::findOrFail($id);
    $recordatorio->ativo = !$recordatorio->ativo;
    $recordatorio->save();

    return redirect()->route('web.recordatorios.index')->with('success', 'Estado do recordatorio atualizado.');
}
}
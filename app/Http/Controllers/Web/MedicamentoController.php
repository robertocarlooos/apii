<?php

namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use App\Models\Medicamento;
use App\Models\Categoria;
use Illuminate\Http\Request;

class MedicamentoController extends Controller
{
    public function index(Request $request)
    {
       
        $query = Medicamento::with('categoria')
        ->where('usuario_id', auth()->id());

    
    if ($request->filled('search')) {
        $query->where('nome', 'LIKE', '%' . $request->search . '%');
    }

    
    $medicamentos = $query->paginate(6);

    
    return view('medicamentos.index', compact('medicamentos'));
    }

    public function show(Medicamento $medicamento)
    {
        // Verificar se o medicamento pertence ao usuario autenticado
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'Não tem permisões para ver este medicamento.');
        }

        return view('medicamentos.show', compact('medicamento'));
    }

    public function create()
    {
        $categorias = Categoria::all();

        // Retorna à view de criação de medicamentos com as categorías disponiveis
        return view('medicamentos.create', compact('categorias'));
    }

    public function store(Request $request)
    {
        // Validar os dados do formulario
        $validatedData = $request->validate([
            'nome' => 'required|string|max:255',
            'dose' => 'required|string|max:255',
            'tipo' => 'required|string|max:255',
            'quantidade_atual' => 'required|integer|min:0',
           
            'categoria_id' => 'nullable|exists:categorias,id',
        ]);

        
        $validatedData['usuario_id'] = auth()->id();

        
        Medicamento::create($validatedData);

        return redirect()->route('web.medicamentos.index')->with('success', 'Medicamento criado com éxito.');
    }

    public function edit(Medicamento $medicamento)
    {
        
        
         if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'No tienes permiso para editar este medicamento.');
        }
    
        $categorias = Categoria::all();
        $medicamentos = Medicamento::where('usuario_id', auth()->id())->get();
    
        return view('medicamentos.edit', compact('medicamento', 'categorias', 'medicamentos'));
    }

    public function update(Request $request, Medicamento $medicamento)
    {
        $validatedData = $request->validate([
            'nome' => 'required|string|max:255',
            'dose' => 'required|string|max:255',
            'tipo' => 'required|string|max:255',
            'quantidade_atual' => 'required|integer|min:0',
           
            'categoria_id' => 'nullable|exists:categorias,id',
        ]);
    
      
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'Não tem permisões para atualizar este medicamento.');
        }
    
        $medicamento->update($validatedData);
    
        return redirect()->route('web.medicamentos.index')->with('success', 'Medicamento atualizado co, éxito.');
    }

    public function destroy(Medicamento $medicamento)
    {
        
        if ($medicamento->usuario_id !== auth()->id()) {
            abort(403, 'No tienes permiso para eliminar este medicamento.');
        }

       
        $medicamento->delete();

        return redirect()->route('web.medicamentos.index')->with('success', 'Medicamento eliminado con éxito.');
    }


    public function marcarYReducir(Request $request, $id)
{
    // Procura p medicamento por ID e verifica se pertence ao usuario autenticado
    $medicamento = Medicamento::where('id', $id)
        ->where('usuario_id', auth()->id())
        ->firstOrFail();

    // Reducir a quantidade atual por 1 se é maior que 0
    if ($medicamento->quantidade_atual > 0) {
        $medicamento->quantidade_atual -= 1;
        $medicamento->save();
    }

    // Registrar no historico de tomas
    \App\Models\HistorialToma::create([
        'medicamento_id' => $medicamento->id,
        'data_toma' => now()->toDateString(),
        'hora_toma' => now()->toTimeString(),
        'estado' => 'Tomado',
    ]);

    // Redirigir com uma mensagem de éxito
    return redirect()->route('web.medicamentos.index')->with('success', 'Medicamento marcado como tomado e quantidade atualizada.');
}
}
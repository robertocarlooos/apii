<?php
 
 namespace App\Http\Controllers\Web;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;

class HistoricoController extends Controller

{
    public function index(Request $request)
    {
           
    $query = \App\Models\HistorialToma::whereHas('medicamento', function ($query) use ($request) {
        $query->where('usuario_id', auth()->id());

        
        if ($request->filled('search')) {
            $query->where('nome', 'LIKE', '%' . $request->search . '%');
        }
    })->with('medicamento');

    
    $historico = $query->paginate(6);

    return view('historico.index', compact('historico'));
    }
}
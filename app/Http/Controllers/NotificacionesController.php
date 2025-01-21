<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class NotificacionesController extends Controller
{
    
    public function index()
    {
        $notificacionesNoLeidas = auth()->user()->unreadNotifications;
        $notificacionesLeidas = auth()->user()->readNotifications;

        return view('notificaciones.index', compact('notificacionesNoLeidas', 'notificacionesLeidas'));
    }

    
    public function marcarComoLeida($id)
    {
        $notificacion = auth()->user()->notifications()->find($id);

        if ($notificacion) {
            $notificacion->markAsRead();
            return redirect()->back()->with('success', 'Notificación marcada como leída.');
        }

        return redirect()->back()->with('error', 'Notificación no encontrada.');
    }

    
    
    public function marcarTodasComoLeidas()
    {
        auth()->usario()->unreadNotifications->markAsRead();

        return redirect()->back()->with('success', 'Todas las notificaciones han sido marcadas como leídas.');
    }

  
    public function eliminar($id)
    {
        $notificacion = auth()->user()->notifications()->find($id);

        if ($notificacion) {
            $notificacion->delete();
            return redirect()->back()->with('success', 'Notificación eliminada con éxito.');
        }

        return redirect()->back()->with('error', 'Notificación no encontrada.');
    }

   
    public function eliminarTodas()
    {
        auth()->user()->notifications()->delete();

        return redirect()->back()->with('success', 'Todas las notificaciones han sido eliminadas.');
    }
}

"use client";
import { Button } from "@/components/ui/button";
import { Users, Calendar, Shield, TrendingUp } from "lucide-react";
import Link from "next/link";

export default function Home() {
  return (
    <div className="container mx-auto px-4 py-8">
      {/* Hero Section */}
      <div className="text-center space-y-6 mb-12">
        <h1 className="text-4xl md:text-6xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
          Bienvenido a Vacuandes
        </h1>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
          Tu plataforma integral para la gestión de vacunación. Administra ciudadanos, programa citas y mantén un control eficiente del proceso de vacunación.
        </p>
      </div>

      {/* Quick Actions */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-12">
        <div className="p-6 rounded-lg border bg-card text-card-foreground shadow-sm hover:shadow-md transition-shadow">
          <div className="flex items-center space-x-4 mb-4">
            <div className="p-2 rounded-md bg-blue-100 text-blue-600">
              <Users className="h-6 w-6" />
            </div>
            <h3 className="text-xl font-semibold">Gestión de Ciudadanos</h3>
          </div>
          <p className="text-muted-foreground mb-4">
            Registra nuevos ciudadanos y gestiona su información personal para el proceso de vacunación.
          </p>
          <Button asChild>
            <Link href="/ciudadanos">
              Gestionar Ciudadanos
            </Link>
          </Button>
        </div>

        <div className="p-6 rounded-lg border bg-card text-card-foreground shadow-sm hover:shadow-md transition-shadow">
          <div className="flex items-center space-x-4 mb-4">
            <div className="p-2 rounded-md bg-green-100 text-green-600">
              <Calendar className="h-6 w-6" />
            </div>
            <h3 className="text-xl font-semibold">Consulta de Citas</h3>
          </div>
          <p className="text-muted-foreground mb-4">
            Consulta las citas de vacunación programadas utilizando el ID del ciudadano.
          </p>
          <p className="text-sm text-muted-foreground">
            Usa la barra de búsqueda en la parte superior para buscar citas por ID de ciudadano.
          </p>
        </div>
      </div>

      {/* Features */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="text-center space-y-4">
          <div className="mx-auto p-3 rounded-full bg-purple-100 text-purple-600 w-fit">
            <Shield className="h-8 w-8" />
          </div>
          <h4 className="text-lg font-semibold">Seguridad</h4>
          <p className="text-sm text-muted-foreground">
            Manejo seguro de datos personales y médicos con los más altos estándares de protección.
          </p>
        </div>

        <div className="text-center space-y-4">
          <div className="mx-auto p-3 rounded-full bg-orange-100 text-orange-600 w-fit">
            <TrendingUp className="h-8 w-8" />
          </div>
          <h4 className="text-lg font-semibold">Eficiencia</h4>
          <p className="text-sm text-muted-foreground">
            Optimiza el proceso de vacunación con herramientas diseñadas para maximizar la eficiencia.
          </p>
        </div>

        <div className="text-center space-y-4">
          <div className="mx-auto p-3 rounded-full bg-cyan-100 text-cyan-600 w-fit">
            <Users className="h-8 w-8" />
          </div>
          <h4 className="text-lg font-semibold">Gestión Integral</h4>
          <p className="text-sm text-muted-foreground">
            Administra todos los aspectos del proceso de vacunación desde una sola plataforma.
          </p>
        </div>
      </div>
    </div>
  );
}

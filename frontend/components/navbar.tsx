"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Sheet, SheetContent, SheetTrigger } from "@/components/ui/sheet";
import { Menu, Users, Calendar, Search } from "lucide-react";

export function Navbar() {
  const [idCiudadano, setIdCiudadano] = useState<string>("");
  const [isOpen, setIsOpen] = useState(false);
  const router = useRouter();

  const handleCitasSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (idCiudadano.length > 0) {
      router.push(`/citas/${idCiudadano}`);
      setIsOpen(false); // Close mobile menu after navigation
    }
  };

  return (
    <nav className="border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <div className="container flex h-16 items-center justify-between px-4">
        {/* Logo */}
        <Link href="/" className="flex items-center space-x-2">
          <div className="flex h-8 w-8 items-center justify-center rounded-md bg-primary text-primary-foreground">
            <span className="text-sm font-bold">V</span>
          </div>
          <span className="text-xl font-bold">Vacuandes</span>
        </Link>

        {/* Desktop Navigation */}
        <div className="hidden md:flex md:items-center md:space-x-6">
          <Link
            href="/ciudadanos"
            className="flex items-center space-x-2 text-sm font-medium transition-colors hover:text-primary"
          >
            <Users className="h-4 w-4" />
            <span>Ciudadanos</span>
          </Link>

          <form onSubmit={handleCitasSearch} className="flex items-center space-x-2">
            <div className="relative">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
              <Input
                type="number"
                placeholder="Buscar citas por ID"
                onChange={(e) => setIdCiudadano(e.target.value)}
                className="w-[200px] pl-9 [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                minLength={1}
                maxLength={10}
                required
              />
            </div>
            <Button type="submit" size="sm">
              <Calendar className="h-4 w-4 mr-2" />
              Ver Citas
            </Button>
          </form>
        </div>

        {/* Mobile Navigation */}
        <Sheet open={isOpen} onOpenChange={setIsOpen}>
          <SheetTrigger asChild className="md:hidden">
            <Button variant="ghost" size="icon">
              <Menu className="h-5 w-5" />
              <span className="sr-only">Abrir menú</span>
            </Button>
          </SheetTrigger>
          <SheetContent side="right" className="w-[300px] sm:w-[400px]">
            <div className="flex flex-col space-y-6 mt-6">
              <Link
                href="/ciudadanos"
                className="flex items-center space-x-3 text-lg font-medium transition-colors hover:text-primary"
                onClick={() => setIsOpen(false)}
              >
                <Users className="h-5 w-5" />
                <span>Gestionar Ciudadanos</span>
              </Link>

              <div className="space-y-4">
                <h3 className="text-lg font-medium">Búsqueda de Citas</h3>
                <form onSubmit={handleCitasSearch} className="space-y-4">
                  <div className="relative">
                    <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                    <Input
                      type="number"
                      placeholder="ID del ciudadano"
                      onChange={(e) => setIdCiudadano(e.target.value)}
                      className="pl-9 [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
                      minLength={1}
                      maxLength={10}
                      required
                    />
                  </div>
                  <Button type="submit" className="w-full">
                    <Calendar className="h-4 w-4 mr-2" />
                    Ver Citas
                  </Button>
                </form>
              </div>
            </div>
          </SheetContent>
        </Sheet>
      </div>
    </nav>
  );
} 
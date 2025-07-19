"use client";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function Home() {
  const [idCiudadano, setIdCiudadano] = useState<string>("");
  const router = useRouter();
  return (
    <div className="font-sans grid grid-rows-[20px_1fr_20px] items-center justify-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 max-w-screen-lg mx-auto">
      <main className="flex flex-col gap-[32px] row-start-2 items-center sm:items-start">
        <h1 className="text-4xl font-bold">Vacuandes</h1>
        <h2 className="text-2xl font-bold">
          Bienvenido a Vacuandes. Acá podrás gestionar todo lo relacionado a
          vacunación desde una sola plataforma.
        </h2>
        <div className="flex flex-col gap-4">
          <h3 className="text-lg font-bold">Ciudadanos</h3>
          <Button>
            <Link href="/ciudadanos">Registrar ciudadano</Link>
          </Button>
          <h3 className="text-lg font-bold">Citas</h3>
          <form
            className="flex flex-row gap-4"
            onSubmit={(e) => {
              e.preventDefault();
              if (idCiudadano.length > 0) {
                router.push(`/citas/${idCiudadano}`);
              }
            }}
          >
            <Input
              type="number"
              placeholder="Buscar citas por ID de ciudadano"
              onChange={(e) => setIdCiudadano(e.target.value)}
              className="w-[300px] [&::-webkit-outer-spin-button]:appearance-none [&::-webkit-inner-spin-button]:appearance-none"
              minLength={1}
              maxLength={10}
              required
            />
            <Button type="submit">Ver citas</Button>
          </form>
        </div>
      </main>
    </div>
  );
}

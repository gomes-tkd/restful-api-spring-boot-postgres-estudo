import Login from "@/components/login/login";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "Login",
    description: "Login to your account",
}

export default async function LoginPage() {
    return (
        <div>
            <Login />
        </div>
    )
}
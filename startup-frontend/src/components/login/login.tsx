import React from 'react';
import Image from "next/image";
import styles from "./login.module.css";
import Logo from "../../../assets/logo.svg";
import Padlock from "../../../assets/padlock.png";
import Input from "@/components/Input/Input";
import Button from "@/components/Button/Button";

function Login() {
    return (
        <div className={styles.loginContainer}>
            <section className={styles.form}>
                <Image src={Logo} alt="logo" width={100} height={100} />
                <form action="">
                    <h1>Access your Account</h1>
                    <Input placeholder={"Username"} />
                    <Input type={"password"} placeholder={"Password"} />
                </form>
                <Button type="submit">Login</Button>
            </section>
            <Image src={Padlock} alt="Logo" />
        </div>
    );
}

export default Login;
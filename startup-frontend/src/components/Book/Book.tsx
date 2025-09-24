import React from 'react';
import Logo from "../../../assets/logo.svg";
import styles from "./book.module.css";
import Image from "next/image";
import Link from "next/link";
import stylesButton from "@/components/Button/button.module.css";
import { FiPower } from "react-icons/fi";

function Book() {
    return (
        <div className={styles.bookContainer}>
            <header>
                <Image src={Logo} alt={"Logo"} width={100} height={100} />
                <span>Welcome, <strong>Gomes</strong>!</span>
                <Link className={stylesButton.button} href={"/book/add"}>Add New Book</Link>
                <button>
                    <FiPower size={18} color={"#251FC5"} />
                </button>
            </header>
        </div>
    );
}

export default Book;
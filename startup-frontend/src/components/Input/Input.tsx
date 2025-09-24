import React from 'react';
import styles from "./input.module.css";

type InputProps = React.ComponentProps<'input'> & {
    error?: string;
};

function Input({ error, ...props }: InputProps) {
    return (
        <>
            <input
                className={styles.input}
                type={props.type}
                id={props.name}
                name={props.name}
                {...props}
            />
            {error && <p className={styles.error}>{error}</p>}
        </>
    );
}

export default Input;
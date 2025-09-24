import { Roboto, Montserrat } from "next/font/google";

export const roboto = Roboto({
    subsets: ["latin"],
    weight: ["400", "500", "700"],
    variable: "--type-first-font-roboto",
    display: "swap",
});

export const montserrat = Montserrat({
    subsets: ["latin"],
    weight: ["500", "700"],
    variable: "--type-second-font-montserrat",
    display: "swap",
});

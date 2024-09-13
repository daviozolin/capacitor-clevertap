import { CleverTap } from 'capacitor-clevertap';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    CleverTap.echo({ value: inputValue })
}

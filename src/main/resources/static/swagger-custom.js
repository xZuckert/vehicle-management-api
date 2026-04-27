window.onload = function () {

    const ui = window.ui;

    // intercepta login
    const originalFetch = window.fetch;

    window.fetch = async function (...args) {
        const response = await originalFetch(...args);

        // se for login
        if (args[0].includes('/auth/login') && response.ok) {
            const data = await response.clone().json();

            if (data.token) {
                // salva token
                localStorage.setItem("jwt", data.token);

                // aplica automaticamente no swagger
                ui.preauthorizeApiKey("bearerAuth", "Bearer " + data.token);
            }
        }

        return response;
    };

    // se já tiver token salvo
    const token = localStorage.getItem("jwt");
    if (token) {
        ui.preauthorizeApiKey("bearerAuth", "Bearer " + token);
    }
};
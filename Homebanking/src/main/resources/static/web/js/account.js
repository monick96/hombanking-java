Vue.createApp({

    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
            messageDelete:false
        }
    },
    methods: {
        getData: function () {
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');

            axios.get(`/api/accounts/${id}`)
                .then((response) => {
                    //get client ifo
                    this.accountInfo = response.data;
                    //console.table(this.accountInfo);

                    //verify is this.transactions exists and if is a not empty array
                    if (this.accountInfo.transactions && this.accountInfo.transactions.length > 0) {

                        //Filter active transactions from that account
                        this.accountInfo.transactions = this.accountInfo.transactions.filter(transaction => transaction.active);
                        //console.log("Transacciones activas:", this.accountInfo.transactions);

                        //order transactions
                        this.accountInfo.transactions.sort((a, b) => parseInt(b.id - a.id))
                        //console.log(this.accountInfo);

                    }

                })
                .catch((error) => {
                    // handle error
                    console.error("Error en la promesa Axios:", error);
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        confirmDeactivateAccount: function () {
                const urlParams = new URLSearchParams(window.location.search);
                const id = urlParams.get('id');

                // Get the current account balance
                const currentBalance = this.accountInfo.balance;

                // Check if the account has a positive balance
                if (currentBalance > 0) {
                    this.errorMsg = "You cannot delete an account with a positive balance.";
                    this.errorToats.show();
                    return; // Stop the function execution
                }

                // Open Modal confirmation
                    this.showModal.show();

                // Use a confirmation dialog to confirm deletion
                   // const userConfirmed = confirm("Are you sure you want to delete this account? This action cannot be undone.");

        },
        deactivateAccount: function () {
              const urlParams = new URLSearchParams(window.location.search);
              const id = urlParams.get('id');

             // Change the account's local status to inactive
             this.accountInfo.isActive = false;


            // Send a request to the backend to update the state in the database
            axios.patch(`/api/accounts/${id}`)
                .then((response) => {
                    this.messageDelete = true;

                    setTimeout(() => {
                        // Redireccionar a la página después del retraso
                        window.location.href = "/web/accounts.html";
                    }, 2000); // 2000 milisegundos = 2 segundos
                })
                .catch((error) => {
                    this.errorMsg = "Error deleting account";
                    this.errorToats.show();
                })
            // close modal confirmation
            this.showModal.hide();
        },
        formatDate: function (date) {

            return new Date(date).toLocaleDateString('en-gb');

        },
        signOut: function () {
            axios.post('/api/logout')
                .then(response => window.location.href = "/web/index.html")
                .catch(() => {
                    this.errorMsg = "Sign out failed"
                    this.errorToats.show();
                })
        },
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.showModal = new bootstrap.Modal(document.getElementById('confirmationModal'));
        this.getData();
    }
}).mount('#app')
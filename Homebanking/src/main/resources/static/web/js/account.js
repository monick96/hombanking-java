Vue.createApp({

    data() {
        return {
            accountInfo: {},
            errorToats: null,
            errorMsg: null,
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
                    this.accountInfo.transactions.sort((a, b) => parseInt(b.id - a.id))
                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
                    this.errorToats.show();
                })
        },
        deactivateAccount: function () {
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

                // Use a confirmation dialog to confirm deletion
                    const userConfirmed = confirm("Are you sure you want to delete this account? This action cannot be undone.");

                axios.patch(`/api/accounts/${id}`)
                    .then((response) => {
                        alert('The account has been successfully deleted.');
                        window.location.href = "/web/accounts.html"
                    })
                    .catch((error) => {
                        this.errorMsg = "Error deleting account";
                        this.errorToats.show();
                    })
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
        this.getData();
    }
}).mount('#app')
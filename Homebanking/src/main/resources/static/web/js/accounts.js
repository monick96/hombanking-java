Vue.createApp({

    data() {
        return {
            activeAccounts:{
            },
            clientInfo: {
            },
            errorToats: null,
            errorMsg: null,
            typeAccount:null,

        }
    },
    methods: {
        getData: function () {
            axios.get("/api/clients/current")
                .then((response) => {
                    //get client ifo
                    this.clientInfo = response.data;
                    this.activeAccounts = this.clientInfo.accounts.filter(account => account.active)
                    //console.log(this.clientInfo);
                    //console.table(this.clientInfo);
                    //console.log(this.activeAccounts);
                    //console.table(this.activeAccounts);

                })
                .catch((error) => {
                    // handle error
                    this.errorMsg = "Error getting data";
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
        create: function () {
            axios.post('/api/clients/current/accounts',`typeAccount=${this.typeAccount}`)
                .then(response => window.location.reload())
                .catch((error) => {
                    this.errorMsg = error.response.data;
                    this.errorToats.show();
                })
        }
    },
    mounted: function () {
        this.errorToats = new bootstrap.Toast(document.getElementById('danger-toast'));
        this.getData();

    }
}).mount('#app')


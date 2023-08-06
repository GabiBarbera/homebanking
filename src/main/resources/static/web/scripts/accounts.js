const { createApp } = Vue

const url = 'http://localhost:8080/api/clients/1'

createApp({
    data() {
        return {
            accounts: [],
            clients: [],
        }
    },
    created() {
        this.loadData()
    },
    methods: {
        loadData() {
            axios.get(url)
                .then(response => {
                    this.clients = response.data
                    this.accounts = response.data.accounts
                    this.jsonRest = JSON.stringify(response.data, null, 1);
                })
                .catch(error => console.error('Error:', error));
        }
    }
}).mount('#app')
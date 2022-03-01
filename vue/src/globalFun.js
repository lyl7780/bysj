import Vue from "vue"

Vue.mixin({
    methods:{
        hasAuth(perm){
            let authorities = this.$store.state.menus.permList
            return authorities.indexOf(perm) > -1
        }
    }
})

<template>
  <v-container>
    <v-btn class="v-btn--round pa-1 red" @click="triggerCloseDialog">
      <v-icon color="white">mdi-close</v-icon>
    </v-btn>
    <v-card class="pa-5">
      <v-card-title>
        <span class="headline">Sign Up</span>
      </v-card-title>

      <v-card-text>
        <v-container>
          <v-form ref="form" v-model="valid" lazy-validation>
            <v-text-field
              outlined
              v-model="fName"
              :rules="nameRules"
              label="First Name"
              required
            />

            <v-text-field
              outlined
              v-model="lName"
              :rules="nameRules"
              label="Last Name"
              required
            />

            <v-text-field
              outlined
              v-model="uName"
              :rules="nameRules"
              label="Username"
              required
            />

            <v-text-field
              outlined
              v-model="email"
              :rules="emailRules"
              label="E-mail"
              required
            />
            <v-text-field
              v-model="password"
              :append-icon="show_password ? 'mdi-eye' : 'mdi-eye-off'"
              :rules="[passRules.required, passRules.min]"
              :type="show_password ? 'text' : 'password'"
              name="input-10-1"
              label="Password"
              hint="At least 8 characters"
              outlined
              @click:append="show_password = !show_password"
            />

            <v-btn class="v-btn--rounded mr-5" @click="signupUser">
              Sign Up
            </v-btn>
            <span class="mr-5">Or</span>
            <v-btn class="v-btn--rounded" @click="loginUser">
              Login
            </v-btn>
          </v-form>
        </v-container>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
export default {
  props: {
    dialog: {
      type: Boolean,
      required: true
    }
  },
  name: "SignUp",

  data() {
    return {
      show_password: false,
      password: "",
      passRules: {
        required: value => !!value || "Required",
        min: v => v.length >= 8 || "Min 8 characters"
      }
    };
  },

  methods: {
    triggerCloseDialog() {
      this.$emit("close-dialog", { dialog: true });
    }
  }
};
</script>

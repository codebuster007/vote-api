<template>
  <v-container>
    <v-card class="pa-5">
      <v-card-title>Sign Up</v-card-title>
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
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "signup",

  data() {
    return {
      show_password: false,
      password: "",
      passRules: {
        required: value => !!value || "Required",
        min: v => v.length >= 8 || "Min 8 characters"
      }
    };
  }
};
</script>

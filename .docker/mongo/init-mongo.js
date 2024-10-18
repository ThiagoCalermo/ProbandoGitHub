db.createUser({
  user: "local",
  password: "secret",
  roles: [
    {
      role: "readWrite",
      db: "grupo13db"
    }
  ]
});
db.createUser({
  user: "tet",
  password: "test",
  roles: [
    {
      role: "readWrite",
      db: "grupo13db"
    }
  ]
});
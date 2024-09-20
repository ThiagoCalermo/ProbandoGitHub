db.createUser({
  user: "tet",
  pwd: "test",
  roles: [
    {
      role: "readWrite",
      db: "grupo13db"
    }
  ]
});
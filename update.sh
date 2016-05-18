#!/bin/sh

cp ../../djondb_3.6/db/includes/* native/includes/

cp ../../djondb_3.6/db/build/bson/libdjon-bson.a native/libs/
cp ../../djondb_3.6/db/build/cache/libdjon-cache.a native/libs/
cp ../../djondb_3.6/db/build/driverbase/libdjon-client.dylib native/libs/
cp ../../djondb_3.6/db/build/driverbase/libdjon-clientStatic.a native/libs/
cp ../../djondb_3.6/db/build/command/libdjon-command.a native/libs/
cp ../../djondb_3.6/db/build/db/libdjon-db.a native/libs/
cp ../../djondb_3.6/db/build/filesystem/libdjon-filesystem.a native/libs/
cp ../../djondb_3.6/db/build/network/libdjon-network.a native/libs/
cp ../../djondb_3.6/db/build/service/libdjon-service.a native/libs/
cp ../../djondb_3.6/db/build/tx/libdjon-tx.a native/libs/
cp ../../djondb_3.6/db/build/util/libdjon-util.a native/libs/
cp ../../djondb_3.6/db/third_party/libs/libantlr3c.a native/libs/

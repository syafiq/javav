  public String toString(int index, boolean standardConformingStrings) {
    --index;
    if (paramValues[index] == null) {
      return "?";
    } else if (paramValues[index] == NULL_OBJECT) {
      return "NULL";
    } else if ((flags[index] & BINARY) == BINARY) {
      // handle some of the numeric types

      switch (paramTypes[index]) {
        case Oid.INT2:
          short s = ByteConverter.int2((byte[]) paramValues[index], 0);
          return Short.toString(s);

        case Oid.INT4:
          int i = ByteConverter.int4((byte[]) paramValues[index], 0);
          return Integer.toString(i);

        case Oid.INT8:
          long l = ByteConverter.int8((byte[]) paramValues[index], 0);
          return Long.toString(l);

        case Oid.FLOAT4:
          float f = ByteConverter.float4((byte[]) paramValues[index], 0);
          if (Float.isNaN(f)) {
            return "'NaN'::real";
          }
          return Float.toString(f);

        case Oid.FLOAT8:
          double d = ByteConverter.float8((byte[]) paramValues[index], 0);
          if (Double.isNaN(d)) {
            return "'NaN'::double precision";
          }
          return Double.toString(d);

        case Oid.UUID:
          String uuid =
              new UUIDArrayAssistant().buildElement((byte[]) paramValues[index], 0, 16).toString();
          return "'" + uuid + "'::uuid";

        case Oid.POINT:
          RedshiftPoint pgPoint = new RedshiftPoint();
          pgPoint.setByteValue((byte[]) paramValues[index], 0);
          return "'" + pgPoint.toString() + "'::point";

        case Oid.BOX:
          RedshiftBox pgBox = new RedshiftBox();
          pgBox.setByteValue((byte[]) paramValues[index], 0);
          return "'" + pgBox.toString() + "'::box";
      }
      return "?";
    } else {
      String param = paramValues[index].toString();

      // add room for quotes + potential escaping.
      StringBuilder p = new StringBuilder(3 + (param.length() + 10) / 10 * 11);

      // No E'..' here since escapeLiteral escapes all things and it does not use \123 kind of
      // escape codes
      p.append('\'');
      try {
        p = Utils.escapeLiteral(p, param, standardConformingStrings);
      } catch (SQLException sqle) {
        // This should only happen if we have an embedded null
        // and there's not much we can do if we do hit one.
        //
        // The goal of toString isn't to be sent to the server,
        // so we aren't 100% accurate (see StreamWrapper), put
        // the unescaped version of the data.
        //
        p.append(param);
      }
      p.append('\'');
      int paramType = paramTypes[index];
      if (paramType == Oid.TIMESTAMP) {
        p.append("::timestamp");
      } else if (paramType == Oid.TIMESTAMPTZ) {
        p.append("::timestamp with time zone");
      } else if (paramType == Oid.TIME) {
        p.append("::time");
      } else if (paramType == Oid.TIMETZ) {
        p.append("::time with time zone");
      } else if (paramType == Oid.DATE) {
        p.append("::date");
      } else if (paramType == Oid.INTERVAL) {
        p.append("::interval");
      } else if (paramType == Oid.INTERVALY2M) {
        p.append("::interval year to month");
      } else if (paramType == Oid.INTERVALD2S) {
        p.append("::interval day to second");
      } else if (paramType == Oid.NUMERIC) {
        p.append("::numeric");
      }
      return p.toString();
    }
  }
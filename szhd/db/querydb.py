# -*- coding: utf-8 -*-

import sqlalchemy
from sqlalchemy.orm import Session
from sqlalchemy.ext.automap import automap_base

if __name__ == "__main__":
    import sys
    reload(sys)
    sys.setdefaultencoding('utf-8')

    engine_str = 'sqlite:///szhd.db'
    engine = sqlalchemy.create_engine(engine_str)
    session = Session(engine)

    # 下面这两句话就完成了ORM映射 Base.classes.XXXX即为映射的类
    # Base.metadata.tables['XXX']即为相应的表
    Base = automap_base()
    Base.prepare(engine, reflect = True)

    # 查询操作
    rs = session.query(Base.classes.outline).all()
    for r in rs:
        print "dt=%s,oid=%d, title=%s" % (r.update_dt, r.oid, r.title.encode('utf-8'))
       
    session.close()
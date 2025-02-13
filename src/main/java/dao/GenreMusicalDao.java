package dao;

import domain.GenreMusical;

public class GenreMusicalDao  extends AbstractJpaDao<Long, GenreMusical> {
	 public GenreMusicalDao() {
	        setClazz(GenreMusical.class);
	    }
}

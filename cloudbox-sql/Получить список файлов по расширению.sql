
select *
from cb_file
where fileext = 'doc'


select *
from cb_userfile uf
inner join cb_file f
on uf.uf_fileid = f.fileid
where f.fileext = 'doc' and uf.uf_userid = 1

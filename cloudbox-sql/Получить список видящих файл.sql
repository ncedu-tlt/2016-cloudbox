select * 
from cb_userfile 
join cb_file 
on cb_file.fileid = cb_userfile.uf_fileid 
join cb_user 
on cb_user.userid = cb_userfile.uf_userid 
where uf_fileid = 1
order by cb_file.filename
